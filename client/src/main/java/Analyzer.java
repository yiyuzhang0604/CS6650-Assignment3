import java.util.*;

public class Analyzer {
    private List<RequestResult> localResultList;
    public Analyzer(Queue<RequestResult> globalQueue) {
        localResultList = new ArrayList<>();

        for (RequestResult result: globalQueue) {
            localResultList.add(result);
        }
        Collections.sort(localResultList);
    }

    public double getMeanResponseTime() {
        long sum = 0;
        for (RequestResult result: this.localResultList) {
            sum += result.getLatency();
        }
        return (double) sum/(double)this.localResultList.size();
    }

    public double getMedianResponseTime() {
        if (localResultList.size() % 2 == 0) {
            return (localResultList.get(localResultList.size()/2).getLatency() + localResultList.get(localResultList.size()/2 - 1).getLatency()) * 0.5;

        }
        return localResultList.get(localResultList.size()/2).getLatency();
    }

    public long getP99Response() {
        return localResultList.get((int)Math.ceil(localResultList.size() * 0.99)).getLatency();
    }

    public long getMaxResponse() {
        return localResultList.get(localResultList.size() - 1).getLatency();
    }

    public long getMinResponse() {
        return localResultList.get(0).getLatency();
    }
}
