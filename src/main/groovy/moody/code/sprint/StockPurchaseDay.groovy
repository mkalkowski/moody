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

        StringBuilder sb=  new StringBuilder()
        findPurchaseDays(quotes, prices).each { sb.append(String.valueOf(it) + "\n")}
        System.out.print(sb.toString())

    }


    static List findPurchaseDays(List<Integer> quotes, List<Integer> prices) {

        def low = Integer.MAX_VALUE
        def high = -1
        int lastDayPrice = prices.get(prices.size()-1)
        def result = []
        def startIdx =0
        def bucketz = prices.collate(1000).collect( {

            def b = new Bucket(it, startIdx, Collections.min(it))
            startIdx += 1000
            b
        }).reverse()


        prices.each {
            if (it < low) low = it;
            if (it > high) high = it
        }

        quotes.each {
            def quoteResult
            if (it < low) {
                quoteResult = -1
            } else if (it >= high || it >= lastDayPrice) {
                quoteResult = prices.size()
            } else {
                quoteResult = bucketz.find { b -> b.contains(it)}.findLastDay(it)
            }

            result.add(quoteResult)
        }
        result
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
