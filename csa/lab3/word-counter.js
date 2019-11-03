class WordCounter {
    constructor(src) {
        this.words = src.split(",");
    }

    count() {
        let count = 0;
        let main = this.words[0];

        this.words.forEach(function(word){
            if (word == main){
                count++;
            }
        });

        return count;
    }

    getResult() {
        try {
            return this.count();
        } catch {
            return 0;
        }
    }
}


module.exports = {
    WordCounter,
}
