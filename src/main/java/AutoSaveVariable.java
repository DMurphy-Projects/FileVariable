
public abstract class AutoSaveVariable <T> extends FileVariables<T> {

    public AutoSaveVariable(String saveFile) {
        super(saveFile);
    }

    @Override
    public void putValue(String key, T value) {
        super.putValue(key, value);
        save();
    }

    @Override
    public void putArrayValue(String key, T value, int index) {
        super.putArrayValue(key, value, index);
        save();
    }

    @Override
    public void putArray(String key, T[] array) {
        super.putArray(key, array);
        save();
    }
}
