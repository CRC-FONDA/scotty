package stream.scotty.slicing.slice;

import stream.scotty.slicing.WindowManager;
import stream.scotty.state.StateFactory;


public class SliceFactory<InputType, ValueType> {

    private final WindowManager windowManager;
    private StateFactory stateFactory;

    public SliceFactory(WindowManager windowManager, StateFactory stateFactory) {
        this.windowManager = windowManager;
        this.stateFactory = stateFactory;
    }

    public Slice<InputType, ValueType> createSlice(long startTs, long maxValue, long startCount, long endCount, Slice.Type type) {
        if(!windowManager.hasCountMeasure() && ((!windowManager.hasContextAwareWindow() || windowManager.isSessionWindowCase()) && windowManager.getMaxLateness()>0)){
            return new EagerSlice<>(stateFactory, windowManager, startTs, maxValue, startCount, endCount, type);
        }
        return new LazySlice<>(stateFactory, windowManager, startTs, maxValue, startCount, endCount, type);
    }
    public Slice<InputType, ValueType> createSlice(long startTs, long maxValue, Slice.Type type) {
        return createSlice(startTs, maxValue, windowManager.getCurrentCount(), windowManager.getCurrentCount(), type);
    }


}
