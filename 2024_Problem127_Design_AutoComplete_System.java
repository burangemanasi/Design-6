//642. Design Search Autocomplete System - https://leetcode.com/problems/design-search-autocomplete-system/description/
//Time Complexity: O(n*l)
//Space Complexity: n^2 * l^2
//Brute Force:

class AutocompleteSystem {
    TrieNode root;
    HashMap<String, Integer> map;
    String search;

    class TrieNode{
        TrieNode[] children;
        List<String> startsWith; //to store all words and get at O(1)

        public TrieNode(){
            this.children = new TrieNode[256]; //including space
            this.startsWith = new ArrayList<>();
        }
    }

    private void insert(String word){
        TrieNode curr = root;
        for(char c: word.toCharArray()){
            if(curr.children[c - ' '] == null){
                curr.children[c - ' '] = new TrieNode();
            }
            curr = curr.children[c - ' '];
            curr.startsWith.add(word);
        }
    }

    private List<String> searchPrefix(String prefix){
        TrieNode curr = root;
        for(char c: prefix.toCharArray()){
            if(curr.children[c - ' '] == null){
                return new ArrayList<>();
            }
            curr = curr.children[c - ' '];
        }
        return curr.startsWith;
    }

    public AutocompleteSystem(String[] sentences, int[] times) {
        this.map = new HashMap<>();
        this.search = "";
        this.root = new TrieNode();
        //initially insert all the sentences into map
        for(int i=0; i<sentences.length; i++){
            String sentence = sentences[i];
            int time = times[i];
            if(!map.containsKey(sentence)){
                //also insert it in the trie
                insert(sentence);
            }
            map.put(sentence, map.getOrDefault(sentence, 0)+time);
        }
    }

    public List<String> input(char c) {
        if(c == '#'){
            if(!map.containsKey(search)){
                insert(search);
            }
            map.put(search, map.getOrDefault(search, 0)+1);
            search="";
            return new ArrayList<>();
        }
        search += c;
        PriorityQueue<String> pq = new PriorityQueue<>((a,b) -> {
            if(map.get(a) == map.get(b)){
                return b.compareTo(a);
            }
            return map.get(a) - map.get(b);
        });

        List<String> list = searchPrefix(search);
        for(String sentence: list){
            pq.add(sentence);
            if(pq.size() > 3){
                pq.poll();
            }
        }
        List<String> result = new ArrayList<>();
        while(!pq.isEmpty()){
            result.add(0, pq.poll());
        }
        return result;
    }
}

//Time Complexity: O(1)
//Space Complexity: n^2 * l^2
//Optimal (using Heaps):

class AutocompleteSystem {
    TrieNode root;
    HashMap<String, Integer> map;
    String search;

    class TrieNode{
        TrieNode[] children;
        List<String> topResults; //to store all words and get at O(1)

        public TrieNode(){
            this.children = new TrieNode[256]; //including space
            this.topResults = new ArrayList<>();
        }
    }

    private void insert(String word){
        TrieNode curr = root;
        for(char c: word.toCharArray()){
            if(curr.children[c - ' '] == null){
                curr.children[c - ' '] = new TrieNode();
            }
            curr = curr.children[c - ' '];
            //on each insertion, use Heaps
            List<String> list = curr.topResults;
            if(!list.contains(word)){
                list.add(word);
            }
            Collections.sort(list, (a,b) -> {
                if(map.get(a) == map.get(b)){
                    return a.compareTo(b);
                }
                return map.get(b)-map.get(a);
            });
            if(list.size() > 3){
                list.remove(list.size()-1);
            }
        }
    }

    private List<String> searchPrefix(String prefix){
        TrieNode curr = root;
        for(char c: prefix.toCharArray()){
            if(curr.children[c - ' '] == null){
                return new ArrayList<>();
            }
            curr = curr.children[c - ' '];
        }
        return curr.topResults;
    }

    public AutocompleteSystem(String[] sentences, int[] times) {
        this.map = new HashMap<>();
        this.search = "";
        this.root = new TrieNode();
        //initially insert all the sentences into map
        for(int i=0; i<sentences.length; i++){
            String sentence = sentences[i];
            int time = times[i];
            map.put(sentence, map.getOrDefault(sentence, 0)+time);
            insert(sentence);
        }
    }

    public List<String> input(char c) {
        if(c == '#'){
            map.put(search, map.getOrDefault(search, 0)+1);
            insert(search);
            search="";
            return new ArrayList<>();
        }
        search += c;
        //top 3 elements will be returned
        return searchPrefix(search);
    }
}