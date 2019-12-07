package RAM;

class Node{
    private static int minimal_size = 2;

    private boolean reserved = false;
    private boolean divided = false;

    public int size;
    public int start;

    public Node left_node;
    public Node right_node;

    public Node(int _size, int _start){
        size = _size;
        start = _start;
    }

    public boolean is_divided(){
        return divided;
    }

    public void divide(){
        left_node = new Node(size / 2, start);
        right_node = new Node(size / 2, start + size / 2);
        divided = true;
    }

    public void merge(){
        left_node = null;
        right_node = null;
        divided = false;
    }

    public void reserve(){ reserved = true; }
    public void free(){ reserved = false; }
    public boolean is_reserved(){ return reserved; }

    int find_free_place(int searched_size){
        if(is_reserved()){
            return -1;
        }

        if(size < searched_size){
            return -1;
        }

        if(size < searched_size * 2 || size == minimal_size){
            if(divided) {
                return -1;
            }

            reserve();
            return start;
        } else {
            if(!divided){
                divide();
            }

            int found_left_node = left_node.find_free_place(searched_size);
            if(found_left_node != -1){
                return found_left_node;
            }

            int found_right_node = right_node.find_free_place(searched_size);
            if(found_right_node != -1){
                return found_right_node;
            }
        }

        return -1;
    }

    void free_place(int address){
        if(divided){
            if(address >= right_node.start){
                right_node.free_place(address);
            } else {
                left_node.free_place(address);
            }
        } else {
            if (address == start) {
                free();
                return;
            }
        }


        if(divided){
            if(!right_node.is_reserved() && !left_node.is_reserved() && !right_node.divided && !left_node.divided){
                merge();
            }
        }
    }

    void print_division(){
        if(!divided){
            String status;
            if(reserved){
                status = "Zarezerwowany";
            }else{
                status = "Wolny";
            }
            System.out.println("Od: " + start + ", rozmiar: " + size + ", " + status);
        }else{
            left_node.print_division();
            right_node.print_division();
        }
    }
}
