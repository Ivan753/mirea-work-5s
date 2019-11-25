import java.util.ArrayList;
import java.util.Arrays;

public class WordCounter implements Result {

    private ArrayList<String> words = new ArrayList<>();

    public WordCounter(String src) {
        this.words = new ArrayList<String>(Arrays.asList(src.split(",")));
    }

    public WordCounter(ArrayList<String> src) {
        words = src;
    }

    public WordCounter(String ... src) {
        for(int i = 0; i < src.length; i++) {
            this.words.add(src[i]);
        }
    }

    public Integer count(){
        int count = 0;
        String main = this.words.get(0);

        for (String word : this.words){
            if (word.equals(main)){
                count++;
            }
        }

        return count;
    }

    @Override
    public String getResult() {
        return String.valueOf(this.count());
    }
}
