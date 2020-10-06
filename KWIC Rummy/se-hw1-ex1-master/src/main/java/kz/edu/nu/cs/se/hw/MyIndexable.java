package kz.edu.nu.cs.se.hw;

public class MyIndexable implements Indexable, Comparable{
    String input;
    int lineNumber;
    int index;

    MyIndexable (String word, int line, int num) {
        this.input = word;
        this.lineNumber = line;
        this.index = num;
    }

    @Override
    public String getEntry() {
        return input;
    }

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    public int getInd() {
        return index;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof MyIndexable) {
            MyIndexable obj = (MyIndexable) o;
            if (this.input == obj.input) {
                return this.lineNumber - obj.lineNumber;
            } else {
                return this.input.compareTo(obj.input);
            }
        } else {
            return 1;
        }
    }
}
