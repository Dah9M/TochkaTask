
import java.util.*;


// Собираю интервалы [check_in, check_out) в notes и сортитрую, это O(n * logn).
// Использую мин-кучу heap для отслеживания текущих заселенных гостей. Храню только даты выселения,
// так как даты заселения не нужны, все кто в куче - они уже заселены.
// Пуш и поп в кучу - O(logn). Всего операций в худшем случае O(n * logn)
// Итого: O(n * logn) + O(n * logn) = O(n * logn)
// Также придумал решение через хранение обычных мап в массиве длины 2n
// Формата Map<String, Integer> где String - дата выселения/заселения, а
// Integer это +1 или -1, отсортировать по дате и идти по массиву и прибавлять
// или убавлять и так посчитать O(n * logn) + O(n) = O(n * logn)
// Получается быстрее


public class run {
    public static boolean checkCapacity(int maxCapacity, List<Map<String, String>> guests) {
        List<String[]> notes = new ArrayList<>();

        for (Map<String, String> guest : guests) {

            String checkIn = guest.get("check-in");
            String checkOut = guest.get("check-out");

            String[] current = new String[]{checkIn, checkOut};

            notes.add(current);
        }

        notes.sort(Comparator.comparing((String[] a) -> a[0]).thenComparing(a -> a[1]));

        PriorityQueue<String> heap = new PriorityQueue<>();

        for (String[] note : notes) {
            String checkIn = note[0];
            String checkOut = note[1];

            while (!heap.isEmpty() && heap.peek().compareTo(checkIn) <= 0) {
                heap.poll();
            }

            heap.offer(checkOut);

            if (heap.size() > maxCapacity) {
                return false;
            }
        }

        return true;
    }


    // Вспомогательный метод для парсинга JSON строки в Map
    private static Map<String, String> parseJsonToMap(String json) {
        Map<String, String> map = new HashMap<>();
        // Удаляем фигурные скобки
        json = json.substring(1, json.length() - 1);


        // Разбиваем на пары ключ-значение
        String[] pairs = json.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            String key = keyValue[0].trim().replace("\"", "");
            String value = keyValue[1].trim().replace("\"", "");
            map.put(key, value);
        }

        return map;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        // Первая строка - вместимость гостиницы
        int maxCapacity = Integer.parseInt(scanner.nextLine());


        // Вторая строка - количество записей о гостях
        int n = Integer.parseInt(scanner.nextLine());


        List<Map<String, String>> guests = new ArrayList<>();


        // Читаем n строк, json-данные о посещении
        for (int i = 0; i < n; i++) {
            String jsonGuest = scanner.nextLine();
            // Простой парсер JSON строки в Map
            Map<String, String> guest = parseJsonToMap(jsonGuest);
            guests.add(guest);
        }


        // Вызов функции
        boolean result = checkCapacity(maxCapacity, guests);


        // Вывод результата
        System.out.println(result ? "True" : "False");


        scanner.close();
    }
}