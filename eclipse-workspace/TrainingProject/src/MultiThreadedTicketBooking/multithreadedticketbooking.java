package MultiThreadedTicketBooking;

import java.util.*;

class TicketBookingSystem {
    private Queue<Integer> availableSeats;  // seats available

    public TicketBookingSystem(int totalSeats) {
        availableSeats = new LinkedList<>();
        for (int i = 1; i <= totalSeats; i++) {
            availableSeats.add(i);
        }
    }

    // synchronized method to book tickets
    public synchronized List<Integer> bookTickets(String userName, int numSeats) {
        List<Integer> bookedSeats = new ArrayList<>();

        if (availableSeats.isEmpty()) {
            System.out.println(userName + " requested " + numSeats +
                    " seats → Booking failed (No seats available).");
            return bookedSeats;
        }

        for (int i = 0; i < numSeats && !availableSeats.isEmpty(); i++) {
            bookedSeats.add(availableSeats.poll());
        }

        if (bookedSeats.size() == numSeats) {
            System.out.println(userName + " requested " + numSeats +
                    " seats → Booking successful (Seats: " + bookedSeats + ")");
        } else {
            System.out.println(userName + " requested " + numSeats +
                    " seats → Only " + bookedSeats.size() + " seat(s) available → " +
                    "Booking partially successful (Seats: " + bookedSeats + ")");
        }

        return bookedSeats;
    }

    public synchronized int getRemainingSeats() {
        return availableSeats.size();
    }
}

class User implements Runnable {
    private String userName;
    private TicketBookingSystem bookingSystem;
    private int seatsToBook;

    public User(String userName, TicketBookingSystem bookingSystem, int seatsToBook) {
        this.userName = userName;
        this.bookingSystem = bookingSystem;
        this.seatsToBook = seatsToBook;
    }

    @Override
    public void run() {
        bookingSystem.bookTickets(userName, seatsToBook);
    }
}

public class multithreadedticketbooking {
    public static void main(String[] args) {
        int totalSeats = 50;
        TicketBookingSystem bookingSystem = new TicketBookingSystem(totalSeats);

        Random random = new Random();
        List<Thread> users = new ArrayList<>();

        // create 10 users (threads)
        for (int i = 1; i <= 10; i++) {
            int seatsRequested = random.nextInt(5) + 1; // 1 to 5 seats
            Thread t = new Thread(new User("User-" + i, bookingSystem, seatsRequested));
            users.add(t);
        }

        // start all threads
        for (Thread t : users) {
            t.start();
        }

        // wait for all threads to finish
        for (Thread t : users) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All bookings completed. Remaining seats = " +
                bookingSystem.getRemainingSeats());
    }
}