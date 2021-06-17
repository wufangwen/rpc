package wfw.rpc.test.version6.loadBalance;

import java.util.List;

/**
 * 轮询负载均衡
 */
public class RoundLoadBalance implements LoadBalance{
    private int choose = -1;
    @Override
    public String balance(List<String> addressList) {
        choose++;

        choose = choose%addressList.size();
        return addressList.get(choose);
    }
}
