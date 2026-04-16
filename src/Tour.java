public class Tour {
    private class Node {
        private Point p;     // Valor do ponto no nó
        private Node next;   // Ponteiro para o próximo nó
    }

    private Node start; // Primeiro nó da lista encadeada circular

    /**
     * Cria um tour vazio.
     */
    public Tour() {
        start = null; // Inicializamos como null para facilitar a verificação de vazio
    }

    /**
     * Cria um tour de 4 pontos a -> b -> c -> d -> a (para depuração).
     */
    public Tour(Point a, Point b, Point c, Point d) {
        start = new Node();
        Node b1 = new Node();
        Node c1 = new Node();
        Node d1 = new Node();

        start.p = a;
        b1.p = b;
        c1.p = c;
        d1.p = d;

        start.next = b1;
        b1.next = c1;
        c1.next = d1;
        d1.next = start;
    }

    /**
     * Retorna o número de pontos no tour.
     */
    public int size() {
        if (start == null) return 0;
        
        int counter = 0;
        Node current = start;
        do {
            counter++;
            current = current.next;
        } while (current != start);
        
        return counter;
    }

    /**
     * Retorna o comprimento total do tour (distância euclidiana).
     */
    public double length() {
        if (start == null) return 0.0;
        
        double distance = 0.0;
        Node current = start;
        do {
            distance += current.p.distanceTo(current.next.p);
            current = current.next;
        } while (current != start);

        return distance;
    }

    /**
     * Insere p usando a heurística "Nearest Insertion".
     */
    public void insertNearest(Point p) {
        if (start == null) {
            start = new Node();
            start.p = p;
            start.next = start;
            return;
        }

        // 1. Encontrar o nó já existente mais próximo do novo ponto p
        Node nearestNode = start;
        double minDistance = start.p.distanceTo(p);
        Node current = start.next;

        while (current != start) {
            double d = current.p.distanceTo(p);
            if (d < minDistance) {
                minDistance = d;
                nearestNode = current;
            }
            current = current.next;
        }

        // 2. Inserir o novo nó imediatamente após o nearestNode
        Node newNode = new Node();
        newNode.p = p;
        newNode.next = nearestNode.next;
        nearestNode.next = newNode;
    }

    /**
     * Insere p usando a heurística "Smallest Insertion" (menor aumento de custo).
     */
    public void insertSmallest(Point p) {
        if (start == null) {
            start = new Node();
            start.p = p;
            start.next = start;
            return;
        }

        // 1. Encontrar a aresta (current -> next) que minimiza o incremento total
        Node bestNode = start;
        double minIncrease = Double.POSITIVE_INFINITY;
        Node current = start;

        do {
            double distAP = current.p.distanceTo(p);          // A para P
            double distPB = p.distanceTo(current.next.p);     // P para B
            double distAB = current.p.distanceTo(current.next.p); // Original A para B
            
            double increase = distAP + distPB - distAB;

            if (increase < minIncrease) {
                minIncrease = increase;
                bestNode = current;
            }
            current = current.next;
        } while (current != start);

        // 2. Inserir o novo nó entre bestNode e bestNode.next
        Node newNode = new Node();
        newNode.p = p;
        newNode.next = bestNode.next;
        bestNode.next = newNode;
    }

    public String toString() {
        if (start == null) return "";
        
        StringBuilder str = new StringBuilder();
        Node current = start;
        do {
            str.append(current.p.toString()).append("\n");
            current = current.next;
        } while (current != start);
        return str.toString();
    }

    public void draw() {
        if (start == null) return;
        
        Node current = start;
        do {
            current.p.drawTo(current.next.p);
            current = current.next;
        } while (current != start);
    }

    public static void main(String[] args) {
        // Bloco de teste básico
        Point a = new Point(1.0, 1.0);
        Point b = new Point(1.0, 4.0);
        Point c = new Point(4.0, 4.0);
        Point d = new Point(4.0, 1.0);

        Tour squareTour = new Tour(a, b, c, d);
        System.out.println("Points: " + squareTour.size());
        System.out.println("Length: " + squareTour.length());
        
        Point e = new Point(5.0, 6.0);
        squareTour.insertNearest(e);
        // squareTour.insertSmallest(e); // Descomente para testar a outra
        squareTour.draw();
    }
}