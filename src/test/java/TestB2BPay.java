import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestB2BPay {

    private static final String MERCHANT_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCe0dtUU49jKZBxNNL5+vHG0Cl6VyAwttWhnOoq64KNCpo2EIFG0vkI7S2qhawVztli2nL/KQoYetEq2UWg6Z6F0hhee68sFL+AyK43kSirASgkl/5eja1UQz2usK8/3CiEj0rhxpLis0LLucDKVRQtqZ7TEZ3SUz4PwD4dlq80eZuEwk2F/aGtsljcn36FO30EX++1JIGk5ntB5bXckDeZ+mEhhG06xygL3g6UxwibpK8pTSfOeBKrFvHAK5Swjx9S6+AkH8nzvvlFTX+Y7PahTUUSnPN58H8NvuA09CQXmwa/JcybkaMzAxWnhXswy1OF+MnzzaiwQUQBzqvDUJh1AgMBAAECggEBAJjCSTAm3zgF9oSsxnDjSkJsXjjqLHAuq/SytRlrRTrcA9AUuAOjUR2g49eskwS23KkUbJ+4nZlGKIIKUcqganVUy1O1q5u+qSP54nhYCsHQbwgjmergYuM1edLY5veJjv9RxDi9gvLCcXD6zdDPXIU/LSDfROnUX1FPG2/iS8sv1rLpCnIIpqWAPo73CaAhusuAEJVlE8dpZEUwyZDPPELXvBVF9we6rzkP9gbJqRYEXr+7qQ3tbwqSfGtdD6LFGMXEW95S0SC3n82Ij5mGpQuVW3jm6JtWtUIY9NltjUnfXoUMww8R33DmyNUMvtVEPg7RLfRZEhSI2oXFGr74ewECgYEA4Bxnn0rJ+eZmZeZELVj4kJsXafjUrE3ulj7X7UfQ2EtvQBYdOChuQtf0hgmTiPrFPhC/IgzOhtzi5MIwV27C1NP7UN8ngjAzGSV7wMic7FJaJtRqloAikG1hZP3eB/8+IzTtxmeBe6wMiSXyjPgtS9xxGGTBMIJu/QBUSe4KRdECgYEAtWsd64KNVuuCtayhwGt9BNJwlNp82947qy/FayKXx5bZPOZsA7ajl7d3C2ZX3vQay/fgflnATQ1faXMI2zy+MUXA+On48tapz/TE+PHp7pX+gm5WGGoCXhgcq1aoAivFx3encjLfmublKPWac8sEpn2Gu2WYoo4LAfSIVZFsfWUCgYA3330KHqg6UHHJB96Je7wEuVXeCrZi+s730FUfCB+OPUhQLvM79ACBU7vXyv1oUjToo14zjAPVZa5/n9ZThf629t6aK/h6oa+rnke7KYjN9j4Zni2wteNoJIz6k7xOPBlux5xcMaP7hEpfywEsmhcY6a2cikoL4QjwtKtO/zS6cQKBgQCIKVFXhbDXYAPGnz730LUQfXGKB0JXM0DNhxlBA8GvR4TctIS1dMrsAAMVR0cES2ohVHXl3y75pePKnVA0XC7ThGsFZ3Y1rO/e6PM9dTQCoglXk8CCqm4EeUxrPKr0li3nO+MtTwmMlWfBijTWW7Wtz+DjnakluD6CxSneLzuiEQKBgAmrsnTquMrx9yIGRcRttAVLRFtYkXyQxLOCGOGvIyfDrbqiOoIg5hVto9zjkLxIevBRUqVOuONRpiSo/ftTOtb7ie8cPWjiJz1HHF3g27CJt/jI6/ZpkKJavQOtChtT+VXjLZOTf92FIU44nislpwMexxIlyTuV5ifSk/zFLCeu";
    private static final String MERCHANT_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAntHbVFOPYymQcTTS+frxxtApelcgMLbVoZzqKuuCjQqaNhCBRtL5CO0tqoWsFc7ZYtpy/ykKGHrRKtlFoOmehdIYXnuvLBS/gMiuN5EoqwEoJJf+Xo2tVEM9rrCvP9wohI9K4caS4rNCy7nAylUULame0xGd0lM+D8A+HZavNHmbhMJNhf2hrbJY3J9+hTt9BF/vtSSBpOZ7QeW13JA3mfphIYRtOscoC94OlMcIm6SvKU0nzngSqxbxwCuUsI8fUuvgJB/J8775RU1/mOz2oU1FEpzzefB/Db7gNPQkF5sGvyXMm5GjMwMVp4V7MMtThfjJ882osEFEAc6rw1CYdQIDAQAB";

    private static final String appId = "2020031817080851687";
    private static final String secret = "dVLulrMjORizeuXxdmRWNyizlQwygb";

    private static HqpayClient hqpayClient;

    private static String distribution_trade_no;
    private static String out_trade_no;
    private static String out_refund_no;
    private static String time_expire;


    @BeforeClass
    public static void init() {
    	hqpayClient = new HqpayClient(appId, secret, MERCHANT_PRIVATE_KEY);
        hqpayClient.setHost("http://jicheng.upaypal.cn");//商户在生产环境需要注释这行
        distribution_trade_no = getNo();
        out_trade_no = distribution_trade_no + "_" + (int) (Math.random() * 900 + 100);
        out_refund_no = getNo();
        Calendar cal = Calendar.getInstance();  
        cal.add(Calendar .YEAR, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time_expire = simpleDateFormat.format(cal.getTime());
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
                    put("order_type", "distribution_trade");//distribution_trade 分销交易单 适用于B2B购买场景
                    put("issuer_id", 1819);//卖家商户id
                    put("receiver_id", 6213);//买家商户id
                    put("distribution_trade_no", distribution_trade_no);
                    put("out_trade_no", out_trade_no);
                    put("amount", 0.01);
                    put("subject", "B2B支付订单");
                    put("time_expire", time_expire);
	                Map<String,Object> offerMap = new HashMap<>();
	                offerMap.put("product_id", "\"t12345\"");
	                offerMap.put("product_name", "\"西溪湿地承认门票\"");
	                offerMap.put("quantity", 1);
	                offerMap.put("unit_price", 0.01);
                    put("offer",offerMap);
                }}
        );
        System.out.println("创建订单:" + hqpayClient.sendList(dis_invoices, HqpayResource.CREATE_TRADE));
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
    	Thread.sleep(1000);
        System.out.println("out_refund_no:" + out_refund_no);
        System.out.println("out_trade_no:" + out_trade_no);
        Map<String, Object> refund = new HashMap<String, Object>() {{
            put("out_refund_no", out_refund_no);
            put("out_trade_no", out_trade_no);
            put("refund_amount", "0.01");
            put("refund_reason", "系统自动测试退款");
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
                            put("time_expire", time_expire);
                        }}, new HashMap<String, Object>() {{
                            put("order_type", "distribution_trade");
                            put("issuer_id", 6213);
                            put("receiver_id", 13199);
                            put("distribution_trade_no", "8974894789454455");
                            put("out_trade_no", "8974894789454455_811");
                            put("channel", "upacp_app");
                            put("amount", 0.01);
                            put("subject", "YourSubject");
                            put("time_expire", time_expire);
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
