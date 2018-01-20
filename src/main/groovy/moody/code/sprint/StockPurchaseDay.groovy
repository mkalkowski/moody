package moody.code.sprint

class StockPurchaseDay {


    static void main(String[] args) {

        assert findPurchaseDays([6], [1,4, 6,7,6,9]) == [5]
        assert findPurchaseDays([2, 1, 3, 4, 5], [4 ,2 ,2 ,3, 3, 4 ,6 ,5]) == [3, -1, 5, 6, 8]

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
        def n = br.readLine().toInteger()
        def prices = new ArrayList<Integer>(n)
        br.readLine().split(" ").each { prices.add(it.toInteger())}

        def m = br.readLine().toInteger()
        def quotes = new ArrayList<Integer>(m)
        m.times {
            quotes << br.readLine().toInteger()
        }

        StringBuilder sb =  new StringBuilder()
        findPurchaseDays(quotes, prices).each { sb.append(String.valueOf(it)).append("\n") }
        System.out.print(sb.toString())
    }


    static List findPurchaseDays(List<Integer> quotes, List<Integer> prices) {

        def low = Integer.MAX_VALUE
        def high = -1
        int lastDayPrice = prices.get(prices.size()-1)
        def bucketz = prices.collate(1000).withIndex().collect{ it, idx ->
            new Bucket(it, idx*1000, Collections.min(it))
        }.reverse()


        prices.each {
            if (it < low) low = it;
            if (it > high) high = it
        }

        quotes.collect {
            if (it < low) {
                -1
            } else if (it >= high || it >= lastDayPrice) {
                prices.size()
            } else {
                bucketz.find { b -> b.contains(it)}.findLastDay(it)
            }
        }
    }

    @groovy.transform.Canonical static class Bucket {
        List<Integer> partition
        int start, min

        boolean contains(int quote) {
            quote >= min
        }

        int findLastDay(int quote) {
            for (int i=partition.size()-1; i>=0; i--) {
                if (quote >= partition[i] ) {
                    return start + i + 1
                }
            }
        }
    }

}
