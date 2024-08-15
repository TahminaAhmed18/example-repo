import java.util.*;

public class ENFA {
    private State startState;
    private Set<State> acceptStates;

    public ENFA(State startState, Set<State> acceptStates) {
        this.startState = startState;
        this.acceptStates = acceptStates;
    }

    public State getStartState() {
        return startState;
    }

    public Set<State> getAcceptStates() {
        return acceptStates;
    }

    public static ENFA character(char c) {
        State start = new State(0);
        State accept = new State(1);
        start.addTransition(c, accept);
        return new ENFA(start, Set.of(accept));
    }

    public static ENFA union(ENFA a, ENFA b) {
        State start = new State(0);
        start.addEpsilonTransition(a.getStartState());
        start.addEpsilonTransition(b.getStartState());
        Set<State> acceptStates = new HashSet<>();
        acceptStates.addAll(a.getAcceptStates());
        acceptStates.addAll(b.getAcceptStates());
        return new ENFA(start, acceptStates);
    }

    public static ENFA concat(ENFA a, ENFA b) {
        for (State accept : a.getAcceptStates()) {
            accept.addEpsilonTransition(b.getStartState());
        }
        return new ENFA(a.getStartState(), b.getAcceptStates());
    }

    public static ENFA star(ENFA a) {
        State start = new State(0);
        State accept = new State(1);
        start.addEpsilonTransition(a.getStartState());
        start.addEpsilonTransition(accept);
        for (State acceptState : a.getAcceptStates()) {
            acceptState.addEpsilonTransition(a.getStartState());
            acceptState.addEpsilonTransition(accept);
        }
        return new ENFA(start, Set.of(accept));
    }

    public static ENFA plus(ENFA a) {
        return concat(a, star(a));
    }

    public boolean matches(String input) {
        Set<State> currentStates = epsilonClosure(Set.of(startState));
        for (char c : input.toCharArray()) {
            Set<State> nextStates = new HashSet<>();
            for (State state : currentStates) {
                nextStates.addAll(state.getTransitions(c));
            }
            currentStates = epsilonClosure(nextStates);
        }
        for (State state : currentStates) {
            if (acceptStates.contains(state)) {
                return true;
            }
        }
        return false;
    }

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
