package DeadlockDetectionResolution;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

// Represents a bank account
class Account {
    private final int id;
    private double balance;
    private final ReentrantLock lock = new ReentrantLock();

    public Account(int id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

    @Override
    public String toString() {
        return "Account-" + id + " [balance=" + balance + "]";
    }
}

// Handles transactions between accounts
class Transaction implements Runnable {
    private final Account from;
    private final Account to;
    private final double amount;

    public Transaction(Account from, Account to, double amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public void run() {
        try {
            // Randomly decide lock order to simulate possible deadlock
            if (new Random().nextBoolean()) {
                transfer(from, to, amount);
            } else {
                transfer(to, from, -amount); // reversed order
            }
        } catch (InterruptedException e) {
            System.out.println("[" + Thread.currentThread().getName() + "] Transaction aborted due to deadlock resolution.");
        }
    }

    private void transfer(Account acc1, Account acc2, double amt) throws InterruptedException {
        ReentrantLock lock1 = acc1.getLock();
        ReentrantLock lock2 = acc2.getLock();

        try {
            lock1.lockInterruptibly();
            System.out.println("[" + Thread.currentThread().getName() + "] Locked " + acc1);

            // Simulate delay
            Thread.sleep(100);

            lock2.lockInterruptibly();
            System.out.println("[" + Thread.currentThread().getName() + "] Locked " + acc2);

            // Perform transaction
            if (amt > 0) {
                acc1.withdraw(amt);
                acc2.deposit(amt);
                System.out.println("[" + Thread.currentThread().getName() + "] Transferring " + amt +
                        " from Account-" + acc1.getId() + " to Account-" + acc2.getId());
            } else {
                double positiveAmt = -amt;
                acc2.withdraw(positiveAmt);
                acc1.deposit(positiveAmt);
                System.out.println("[" + Thread.currentThread().getName() + "] Transferring " + positiveAmt +
                        " from Account-" + acc2.getId() + " to Account-" + acc1.getId());
            }
        } finally {
            if (lock2.isHeldByCurrentThread()) lock2.unlock();
            if (lock1.isHeldByCurrentThread()) lock1.unlock();
        }
    }
}

// Detects deadlocks and resolves by interrupting one thread
class DeadlockDetector extends Thread {
    private final ThreadMXBean mbean = ManagementFactory.getThreadMXBean();

    public DeadlockDetector() {
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            long[] deadlockedThreadIds = mbean.findDeadlockedThreads();
            if (deadlockedThreadIds != null) {
                System.out.println("[DeadlockDetector] Deadlock detected!");

                Thread[] allThreads = new Thread[Thread.activeCount()];
                Thread.enumerate(allThreads);

                for (long id : deadlockedThreadIds) {
                    for (Thread t : allThreads) {
                        if (t != null && t.getId() == id) {
                            System.out.println("[DeadlockDetector] Resolving by interrupting " + t.getName());
                            t.interrupt();
                            break;
                        }
                    }
                }
            }
            try {
                Thread.sleep(1000); // check every 1 second
            } catch (InterruptedException ignored) {
            }
        }
    }
}

public class deadlocksimulation {
    public static void main(String[] args) {
        // Create some accounts
        Account acc1 = new Account(1, 1000);
        Account acc2 = new Account(2, 1000);
        Account acc3 = new Account(3, 1000);
        Account acc4 = new Account(4, 1000);

        List<Account> accounts = Arrays.asList(acc1, acc2, acc3, acc4);
        Random rand = new Random();

        // Start the deadlock detector thread
        new DeadlockDetector().start();

        // Start transaction threads
        for (int i = 0; i < 4; i++) {
            Account from = accounts.get(rand.nextInt(accounts.size()));
            Account to;
            do {
                to = accounts.get(rand.nextInt(accounts.size()));
            } while (from == to);

            Thread t = new Thread(new Transaction(from, to, 100), "Transaction-Thread-" + (i + 1));
            t.start();
        }
    }
}
