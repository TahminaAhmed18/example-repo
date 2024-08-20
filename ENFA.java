import java.util.Set;
import java.util.HashSet;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public class ENFA {
    private final State startState;
    private final Set<State> acceptStates;
    private static final AtomicInteger stateCounter = new AtomicInteger(0); // Unique ID generator

    // Constructor
    public ENFA(State startState, Set<State> acceptStates) {
        this.startState = startState;
        this.acceptStates = acceptStates;
    }

    // Getter for start state
    public State getStartState() {
        return startState;
    }

    // Getter for accept states
    public Set<State> getAcceptStates() {
        return acceptStates;
    }

    // Method to create an ENFA for a single character
    public static ENFA character(char c) {
        State start = new State(stateCounter.getAndIncrement());
        State accept = new State(stateCounter.getAndIncrement());
        start.addTransition(c, accept);
        return new ENFA(start, Set.of(accept));
    }

    // Method to match an input string against the ENFA
    public boolean matches(String input) {
        Set<State> currentStates = epsilonClosure(Set.of(startState));
        for (char c : input.toCharArray()) {
            Set<State> nextStates = new HashSet<>();
            for (State state : currentStates) {
                nextStates.addAll(state.getTransitions(c));
            }
            currentStates = epsilonClosure(nextStates);
            if (currentStates.isEmpty()) {
                return false; // Early exit if no valid transitions exist
            }
        }
        // Check if any of the current states are accepting states
        for (State state : currentStates) {
            if (acceptStates.contains(state)) {
                return true;
            }
        }
        return false;
    }

    // Method to compute the epsilon closure of a set of states
    private Set<State> epsilonClosure(Set<State> states) {
        Set<State> closure = new HashSet<>(states);
        Stack<State> stack = new Stack<>();
        closure.forEach(stack::push);
        while (!stack.isEmpty()) {
            State state = stack.pop();
            for (State epsilonState : state.getEpsilonTransitions()) {
                if (!closure.contains(epsilonState)) {
                    closure.add(epsilonState);
                    stack.push(epsilonState);
                }
            }
        }
        return closure;
    }

    // Method to create the union of two ENFAs
    public static ENFA union(ENFA enfa1, ENFA enfa2) {
        State start = new State(stateCounter.getAndIncrement());
        start.addEpsilonTransition(enfa1.getStartState());
        start.addEpsilonTransition(enfa2.getStartState());

        Set<State> acceptStates = new HashSet<>(enfa1.getAcceptStates());
        acceptStates.addAll(enfa2.getAcceptStates());

        return new ENFA(start, acceptStates);
    }

    // Method to concatenate two ENFAs
    public static ENFA concatenate(ENFA enfa1, ENFA enfa2) {
        for (State acceptState : enfa1.getAcceptStates()) {
            acceptState.addEpsilonTransition(enfa2.getStartState());
        }
        return new ENFA(enfa1.getStartState(), enfa2.getAcceptStates());
    }

    // Method to apply the Kleene star operation on an ENFA
    public static ENFA kleeneStar(ENFA enfa) {
        State start = new State(stateCounter.getAndIncrement());
        State accept = new State(stateCounter.getAndIncrement());

        start.addEpsilonTransition(enfa.getStartState());
        start.addEpsilonTransition(accept);

        for (State acceptState : enfa.getAcceptStates()) {
            acceptState.addEpsilonTransition(enfa.getStartState());
            acceptState.addEpsilonTransition(accept);
        }

        return new ENFA(start, Set.of(accept));
    }

    // Method to apply the one or more operation on an ENFA
    public static ENFA oneOrMore(ENFA enfa) {
        State start = new State(stateCounter.getAndIncrement());
        State accept = new State(stateCounter.getAndIncrement());

        start.addEpsilonTransition(enfa.getStartState());

        for (State acceptState : enfa.getAcceptStates()) {
            acceptState.addEpsilonTransition(enfa.getStartState());
            acceptState.addEpsilonTransition(accept);
        }

        return new ENFA(start, Set.of(accept));
    }

    public void printTransitionTable() {
    }
}
