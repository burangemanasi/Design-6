//379. Design Phone Directory - https://leetcode.com/problems/design-phone-directory/
//Time Complexity: O(1) ~ all operations
//Space Complexity: O(maxNumber)

class PhoneDirectory {
    HashSet<Integer> set; //unused maxNumbers
    Queue<Integer> q;
    public PhoneDirectory(int maxNumbers) { //O(n) ~ insertion
        this.set = new HashSet<>();
        this.q = new LinkedList<>();
        //add all numbers initially to the set and queue
        for(int i=0; i<maxNumbers; i++){
            set.add(i);
            q.add(i);
        }
    }

    public int get() {
        if(q.isEmpty()) return -1;
        //get random number from the set
        int num = q.poll(); //used number
        set.remove(num); //get one number from queue and remove from set
        return num;
    }

    public boolean check(int number) {
        //unused number remains in set, used are removed when get() is called
        return set.contains(number);
    }

    public void release(int number) {
        if(set.contains(number)) return;
        //not used anymore, add back to set and queue
        set.add(number);
        q.add(number);
    }
}