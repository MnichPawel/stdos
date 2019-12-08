package RAM;

class BinaryTree {
    private Node main_node;

    public BinaryTree(int size){
        main_node = new Node(size, 0);
    }

    public int reserve_place_for_memory(int size){
        return main_node.find_free_place(size);
    }

    public void free_place_from_memory(int address){
        main_node.free_place(address);
    }

    public void print_division(){
        main_node.print_division();
    }
}
