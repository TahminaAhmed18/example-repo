import java.util.Set;
import java.util.HashSet;
import java.util.Stack;

public class ENFA {
    private State startState;
    private Set<State> acceptStates;

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
        State start = new State(0);
        State accept = new State(1);
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
}
