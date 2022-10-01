
public class Save {
    public static void main(String[] args)
    {
        FileVariables<Integer> fileInt = new FileVariables<Integer>("src/main/resources/save.dat") {
            @Override
            public Integer parseType(String toParse) {
                return Integer.parseInt(toParse);
            }

            @Override
            public Integer[] createArray(int s) {
                return new Integer[s];
            }
        };

        fileInt.putValue("Test", 1);
        fileInt.putArray("Test 2", new Integer[]{1, 2, 3});

        fileInt.save();
    }
}
