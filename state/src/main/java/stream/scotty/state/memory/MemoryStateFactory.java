package stream.scotty.state.memory;

import stream.scotty.state.*;

public class MemoryStateFactory implements StateFactory {
    @Override
    public <T> ValueState<T> createValueState() {
        return new MemoryValueState<>();
    }

    @Override
    public <T> ListState<T> createListState() {
        return new MemoryListState<>();
    }

    @Override
    public <T extends Comparable<T>> SetState<T> createSetState() {
        return new MemorySetState<>();
    }
}
