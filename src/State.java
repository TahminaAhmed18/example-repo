import java.util.*;

public class State {
    private int id;
    private Map<Character, List<State>> transitions;
    private List<State> epsilonTransitions;

    public State(int id) {
        this.id = id;
        this.transitions = new HashMap<>();
        this.epsilonTransitions = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void addTransition(char symbol, State state) {
        transitions.computeIfAbsent(symbol, k -> new ArrayList<>()).add(state);
    }

    public void addEpsilonTransition(State state) {
        epsilonTransitions.add(state);
    }

    public List<State> getTransitions(char symbol) {
        return transitions.getOrDefault(symbol, new ArrayList<>());
    }

    public List<State> getEpsilonTransitions() {
        return epsilonTransitions;
    }
}
