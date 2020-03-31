import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestHqpayClient {

    private static final String MERCHANT_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCkrnzM9yTvHs1ngoYj4+PB5OlTshpbo5iellWu28w6XKwLIiGxDUpDg1nQ1yKyy/+A+7Crupq7RsSw9Xffn/Crn0QMTXAM++eqnoc7/ZFvd1mDUZgFLMcgA0bvnaPOblEEQ4jc8u0OZJiN12A0NV5/e5vNN13y+IsnZPiV20QoVE08Hb+IL7mk2lQlqrt92F0g/+zNv+NmQnsqsFwxwAM7WnvIbdSwGPrEpKVxJPDSYWmEa8MROetLyuYeMiRv0vnseleRC3xh6+ziLy1kKBKHQvHJw5knki6qWz8BE7LCi+2j9Pe6YAEPZ5fp35+bs4XbbiJfLTNsi2Polli3JrrJAgMBAAECggEAEdnYLV+c4U61dULwx3MsfRgTMr9bnBNft9ET+KBawvjIrWxFuqt2IVtboQew+zChwZmGzRatLx+8snHpTJRJwlDBT5QVUVL9CcGlZxtVaH/HdDSkBeuUqiOwzMiJuRa8MXogm9K5X5YH81glVYNzet4rRVN7G5tVei+3wobCHJzZwKNgG96AYCLaFSovg3dbFdD3lxwEf1x0ckiEOMfP+ZBLKPVKukn1S/4EvSyzO/QLHAGzUNTqrYuMx/B4HBM3J4i0ojItlivA8Ci8p2pp4Xx6FKvGoQtVO7T+arjTgxPpDmMgeaHFi0pzKsWeW9zLVyfnLsImnyS5ZNfYx87MAQKBgQDuPpMc+YKD7LHmBb9vYKml+Connj8d/4MA1eT1gdNqHhH7eNJeBA3gHeJ3SAtXwfI4vQ/2WNZ3y9rvJ9uekLf7MacDGFlrFjo+6fFMUhNAKl8v4pJg77kJ56KTW8S9T68+DxLrxRiXxXbH6/pvM2L9YbwZYezfT9zU3rdAu7l59QKBgQCw9Gt5Ei+mxwhZp9ZfOcOGQJ3vj6lKLjWJ8buZYRN9Y52qEVIbAvAFGofrc20v0L+oINfJvwK/hObxpbdi0ypyO/vUFCAzluOV5piEPH5J3KkbOylRICNLttJAaaj35xAj9515n91Z074hGdS3ReUHwaaQPqUJHEXkBvGhOEVVBQKBgDd3hhSm2oYQY1KkX+90V8r2Pgo/Q36ecEePhODLuwbnvVQIyyFsXDajfog607k4xEYFbmPyRuxZH72yT+VY2By5cKK91oOiBymCmdUk/qpfsw2Y+G9tIG136lfYc7OHi8stA0C1AIbFSGQ/Qm0FT7SiPoLCU72iAE3nFE8T5ZjJAoGBAKMbKFUYpkOcAd5iDf8IR2lf9jPyv6BYp5DW+IyQJl7Vi2ZPQNcVBhguNEqawnHogECAxWZ4YhgWcKkcjsTQz7csAxug0W28D1sEimCZQe7jLhyjk5nrfZ00juhAfDbFDOOaQdTxvh4mRoJMRP4JtupHXThoBcc4AFQG8oIgVAfhAoGALlBkazcHNqN5zkYzdOKKvBfXa+5u1l8IoL37sW/GM3jqwRZLRzAODGdw7NDW0EhhNOS3/SBS1syrSjdBAivBBIDS/xUlpO08E7ahqRP7DheXlc1ZChS/izTd9ePNvwy616v57l43NZSe+2jwEFgGXdsxwv9CE/XRFuLfr43ipcE=";

    private static final String MERCHANT_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDUbQNhWV4mhZZseqTrzXSInYCF\n" +
            "lPGi9WcAuxUviGloXVDuAzGkDgURAPnSHB930wDNr6ftVY4UJYoEjKr9DCBs+IIy\n" +
            "4IwVlaLR8IfYFTtjvF+RWs6rqsPO1mwqPmqKSHWtboSw1K3lFIPqO5BAX4jm81uU\n" +
            "hOsi2fG/TADzaVEZvwIDAQAB\n" +
            "-----END PUBLIC KEY-----\n";

    private static HqpayClient hqpayClient;

    private static String distribution_trade_no;
    private static String out_trade_no;
    private static String out_refund_no;


    @BeforeClass
    public static void init() {
        hqpayClient = new HqpayClient("2020031817080851687", "dVLulrMjORizeuXxdmRWNyizlQwygb", MERCHANT_PRIVATE_KEY);
        hqpayClient.setHost("http://jicheng.upaypal.cn");//商户在生产环境需要注释这行
        distribution_trade_no = getNo();
        out_trade_no = getNo() + "_" + (int) (Math.random() * 900 + 100);
        out_refund_no = getNo();
    }

    @Test
    public void test1CreateTrade() throws Exception {

        System.out.println("distribution_trade_no:" + distribution_trade_no);
        System.out.println("out_trade_no:" + out_trade_no);
        /*  1 起始单issuer_id一定要是appid关联的商户id，
         *  2 订单之间issuer_id和receiver_id一定要是链式关系
         *  3 分销单号要一致，订单单号要不同，同时在系统中不存在
         */
        List<Map<String, Object>> dis_invoices = Arrays.asList(
                new HashMap<String, Object>() {{
                    put("order_type", "trade");
                   // put("issuer_id", 1819);
                    //put("receiver_id", 6213);
                    //put("distribution_trade_no", distribution_trade_no);
                    put("out_trade_no", out_trade_no);
                    put("amount", 0.01);
                    
	                Map<String,Object> map = new HashMap<>();
	                map.put("auth_code", "287275545239797330");
	                
	                put("channel","SCAN");
                    put("credential",map);
                    put("subject", "YourSubject");
                    put("time_expire", "2020-03-30 00:00:00");
                }}
        );
        System.out.println("创建分销交易单:" + hqpayClient.sendList(dis_invoices, HqpayResource.CREATE_TRADE));
        Thread.sleep(5000L);
    }

    @Test
    public void test2GetTrade() throws Exception {
        System.out.println("out_trade_no:" + out_trade_no);
        Map<String, Object> getTrade = new HashMap<String, Object>();
        getTrade.put("out_trade_no", out_trade_no);
        System.out.println("查询订单:" + hqpayClient.sendOne(getTrade, HqpayResource.GET_TRADE));
    }

    @Test
    public void test3B2BPay() throws Exception {
        System.out.println("distribution_trade_no" + distribution_trade_no);
        System.out.println("out_trade_no" + out_trade_no);
        Map<String, Object> balancePay = new HashMap<String, Object>() {{
            put("distribution_trade_no", distribution_trade_no);
            put("out_trade_no", out_trade_no);
            put("amount", 0.01);
            put("app_id", 1819);
        }};
        JsonNode jsonNode = hqpayClient.sendOneRtjson(balancePay, HqpayResource.B_2_BPAY);
        System.out.println("支付订单:" + jsonNode);
    }


    @Test
    public void test4CreateRefundBill() throws Exception {
        System.out.println("out_refund_no:" + out_refund_no);
        System.out.println("out_trade_no:" + out_trade_no);
        Map<String, Object> refund = new HashMap<String, Object>() {{
            put("out_refund_no", out_refund_no);
            put("out_trade_no", out_trade_no);
            put("refund_amount", "0.01");
            put("refund_reason", "refund_reason");
        }};
        System.out.println("创建退款单:" + hqpayClient.sendOne(refund, HqpayResource.CREATE_REFUND_BILL));

    }

    @Test
    public void test5GetRefund() throws Exception {
        System.out.println("out_refund_no:" + out_refund_no);
        Map<String, Object> get_refund = new HashMap<String, Object>() {{
            put("out_refund_no", out_refund_no);
        }};
        System.out.println("查询退款单:" + hqpayClient.sendOne(get_refund, HqpayResource.GET_REFUND_BILL));
    }

    @Test
    public void test6Cancel() throws Exception {
        System.out.println("out_trade_no:" + out_trade_no);
        Map<String, Object> cancel = new HashMap<String, Object>() {{
            put("out_trade_no", out_trade_no);
            put("cancel_reason", "cancel_reason");
        }};
        System.out.println("取消交易单:" + hqpayClient.sendOne(cancel, HqpayResource.CANCEL));
    }


    @Test
    public void test7Sign() throws Exception {
        List<Map<String, Object>> pMaps =
                Arrays.asList(
                        new HashMap<String, Object>() {{
                            put("order_type", "distribution_trade");
                            put("issuer_id", 1819);
                            put("receiver_id", 6213);
                            put("distribution_trade_no", "8974894789454455");
                            put("out_trade_no", "8974894789454455_811");
                            put("channel", "upacp_app");
                            put("amount", 0.01);
                            put("subject", "YourSubject");
                            put("time_expire", "2020-12-12 00:00:00");
                        }}, new HashMap<String, Object>() {{
                            put("order_type", "distribution_trade");
                            put("issuer_id", 6213);
                            put("receiver_id", 13199);
                            put("distribution_trade_no", "8974894789454455");
                            put("out_trade_no", "8974894789454455_811");
                            put("channel", "upacp_app");
                            put("amount", 0.01);
                            put("subject", "YourSubject");
                            put("time_expire", "2020-12-12 00:00:00");
                        }}
                );

        for (Map<String, Object> map : pMaps) {
            map.put("signature", HqpaySignature.sign(map, MERCHANT_PRIVATE_KEY));
        }
        System.out.println("签名后的参数:" + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(pMaps));
    }


    private static String getNo() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + (int) (Math.random() * 900 + 100);
    }
}
