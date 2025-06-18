import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Random;

public class ToyShop {
    // Внутренний класс для представления игрушки
    private static class Toy implements Comparable<Toy> {
        int id;
        String name;
        int weight;

        public Toy(int id, String name, int weight) {
            this.id = id;
            this.name = name;
            this.weight = weight;
        }

        @Override
        public int compareTo(Toy other) {
            return Integer.compare(other.weight, this.weight); // Для сортировки по убыванию веса
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Название: " + name + ", Вес: " + weight;
        }
    }

    private PriorityQueue<Toy> toyQueue;
    private Random random;

    // Конструктор принимает минимум 3 строки в формате "id название вес"
    public ToyShop(String... toyData) {
        if (toyData.length < 3) {
            throw new IllegalArgumentException("Необходимо передать минимум 3 игрушки");
        }

        this.toyQueue = new PriorityQueue<>();
        this.random = new Random();

        // Заполняем очередь игрушками
        for (String data : toyData) {
            String[] parts = data.split(" ");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Неверный формат данных: " + data);
            }

            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            int weight = Integer.parseInt(parts[2]);

            toyQueue.add(new Toy(id, name, weight));
        }
    }

    // Метод для получения случайной игрушки с учетом веса
    public Toy getToy() {
        // Создаем временный массив для расчета вероятностей
        int totalWeight = toyQueue.stream().mapToInt(t -> t.weight).sum();
        int randomValue = random.nextInt(totalWeight) + 1;
        int cumulativeWeight = 0;

        for (Toy toy : toyQueue) {
            cumulativeWeight += toy.weight;
            if (randomValue <= cumulativeWeight) {
                return toy;
            }
        }

        return null; // В теории сюда никогда не дойдет
    }

    // Метод для проведения 10 розыгрышей и записи результатов в файл
    public void drawAndSaveResults(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (int i = 0; i < 10; i++) {
                Toy winner = getToy();
                if (winner != null) {
                    String result = "Розыгрыш " + (i + 1) + ": " + winner.name + "\n";
                    System.out.print(result);
                    writer.write(result);
                }
            }
            System.out.println("Результаты сохранены в файл: " + filename);
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Пример использования
        ToyShop shop = new ToyShop(
                "1 Конструктор 20",
                "2 Робот 15",
                "3 Кукла 30",
                "4 Машинка 25",
                "5 Пазл 10"
        );

        // Проводим розыгрыш и сохраняем результаты
        shop.drawAndSaveResults("results.txt");
    }
}
