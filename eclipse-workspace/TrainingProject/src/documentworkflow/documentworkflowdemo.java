package documentworkflow;

import java.util.EnumSet;
import java.util.Set;

// ---------------- Enum defining states and allowed transitions ----------------
enum DocumentState {
    DRAFT,
    REVIEW,
    APPROVED,
    PUBLISHED,
    ARCHIVED;

    private Set<DocumentState> allowedTransitions;

    static {
        DRAFT.allowedTransitions = EnumSet.of(REVIEW);
        REVIEW.allowedTransitions = EnumSet.of(APPROVED, DRAFT);
        APPROVED.allowedTransitions = EnumSet.of(PUBLISHED);
        PUBLISHED.allowedTransitions = EnumSet.of(ARCHIVED);
        ARCHIVED.allowedTransitions = EnumSet.noneOf(DocumentState.class);
    }

    public boolean canTransitionTo(DocumentState nextState) {
        return allowedTransitions.contains(nextState);
    }
}

// ---------------- Document class ----------------
class Document {
    private DocumentState currentState;

    public Document() {
        this.currentState = DocumentState.DRAFT; // Default start
        System.out.println("Document created in state: " + currentState);
    }

    public void transitionTo(DocumentState nextState) {
        if (currentState.canTransitionTo(nextState)) {
            System.out.println("Transitioning from " + currentState + " → " + nextState);
            currentState = nextState;
        } else {
            System.out.println("❌ Invalid transition from " + currentState + " → " + nextState);
        }
    }

    public DocumentState getCurrentState() {
        return currentState;
    }
}

// ---------------- Demo class with main method ----------------
public class documentworkflowdemo {
    public static void main(String[] args) {
        Document doc = new Document();

        // Valid transition: DRAFT → REVIEW
        doc.transitionTo(DocumentState.REVIEW);

        // Invalid transition: REVIEW → PUBLISHED
        doc.transitionTo(DocumentState.PUBLISHED);

        // Valid transition: REVIEW → APPROVED
        doc.transitionTo(DocumentState.APPROVED);

        // Valid transition: APPROVED → PUBLISHED
        doc.transitionTo(DocumentState.PUBLISHED);

        // Valid transition: PUBLISHED → ARCHIVED
        doc.transitionTo(DocumentState.ARCHIVED);

        // Invalid transition: ARCHIVED → REVIEW
        doc.transitionTo(DocumentState.REVIEW);
    }
}