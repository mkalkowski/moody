package moody.code.sprint


class Costs {


    static void main(String[] args) {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
        def (n, m) = br.readLine().split(" ").collect {Integer.parseInt(it)}

        def paidMap = [:]

        m.times { paidMap[it+1] = 0 }
        n.times {

            def (friend, trans) = br.readLine().split(" ").collect {Integer.parseInt(it)}
            paidMap.merge(friend, trans, {a,b -> a+b})
        }

        def total = paidMap.values().sum()
        def equity = (total / m).toInteger()
        def diff = total - equity*m

        paidMap.entrySet().forEach({ it.value -= equity})
        paidMap[1] -= diff
        paidMap.keySet().sort().forEach { println "$it ${paidMap[it]}" }
        println "$total == ${paidMap.values().sum()}"
    }
}
