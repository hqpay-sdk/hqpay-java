import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

/**
 * 支付宝面对付支付-条码支付
 * 微信收款码支付
 * 用户向商家出示支付条码，商家利用扫码设备扫码收款
 */
@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestF2FPay {

    private static final String MERCHANT_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCkrnzM9yTvHs1ngoYj4+PB5OlTshpbo5iellWu28w6XKwLIiGxDUpDg1nQ1yKyy/+A+7Crupq7RsSw9Xffn/Crn0QMTXAM++eqnoc7/ZFvd1mDUZgFLMcgA0bvnaPOblEEQ4jc8u0OZJiN12A0NV5/e5vNN13y+IsnZPiV20QoVE08Hb+IL7mk2lQlqrt92F0g/+zNv+NmQnsqsFwxwAM7WnvIbdSwGPrEpKVxJPDSYWmEa8MROetLyuYeMiRv0vnseleRC3xh6+ziLy1kKBKHQvHJw5knki6qWz8BE7LCi+2j9Pe6YAEPZ5fp35+bs4XbbiJfLTNsi2Polli3JrrJAgMBAAECggEAEdnYLV+c4U61dULwx3MsfRgTMr9bnBNft9ET+KBawvjIrWxFuqt2IVtboQew+zChwZmGzRatLx+8snHpTJRJwlDBT5QVUVL9CcGlZxtVaH/HdDSkBeuUqiOwzMiJuRa8MXogm9K5X5YH81glVYNzet4rRVN7G5tVei+3wobCHJzZwKNgG96AYCLaFSovg3dbFdD3lxwEf1x0ckiEOMfP+ZBLKPVKukn1S/4EvSyzO/QLHAGzUNTqrYuMx/B4HBM3J4i0ojItlivA8Ci8p2pp4Xx6FKvGoQtVO7T+arjTgxPpDmMgeaHFi0pzKsWeW9zLVyfnLsImnyS5ZNfYx87MAQKBgQDuPpMc+YKD7LHmBb9vYKml+Connj8d/4MA1eT1gdNqHhH7eNJeBA3gHeJ3SAtXwfI4vQ/2WNZ3y9rvJ9uekLf7MacDGFlrFjo+6fFMUhNAKl8v4pJg77kJ56KTW8S9T68+DxLrxRiXxXbH6/pvM2L9YbwZYezfT9zU3rdAu7l59QKBgQCw9Gt5Ei+mxwhZp9ZfOcOGQJ3vj6lKLjWJ8buZYRN9Y52qEVIbAvAFGofrc20v0L+oINfJvwK/hObxpbdi0ypyO/vUFCAzluOV5piEPH5J3KkbOylRICNLttJAaaj35xAj9515n91Z074hGdS3ReUHwaaQPqUJHEXkBvGhOEVVBQKBgDd3hhSm2oYQY1KkX+90V8r2Pgo/Q36ecEePhODLuwbnvVQIyyFsXDajfog607k4xEYFbmPyRuxZH72yT+VY2By5cKK91oOiBymCmdUk/qpfsw2Y+G9tIG136lfYc7OHi8stA0C1AIbFSGQ/Qm0FT7SiPoLCU72iAE3nFE8T5ZjJAoGBAKMbKFUYpkOcAd5iDf8IR2lf9jPyv6BYp5DW+IyQJl7Vi2ZPQNcVBhguNEqawnHogECAxWZ4YhgWcKkcjsTQz7csAxug0W28D1sEimCZQe7jLhyjk5nrfZ00juhAfDbFDOOaQdTxvh4mRoJMRP4JtupHXThoBcc4AFQG8oIgVAfhAoGALlBkazcHNqN5zkYzdOKKvBfXa+5u1l8IoL37sW/GM3jqwRZLRzAODGdw7NDW0EhhNOS3/SBS1syrSjdBAivBBIDS/xUlpO08E7ahqRP7DheXlc1ZChS/izTd9ePNvwy616v57l43NZSe+2jwEFgGXdsxwv9CE/XRFuLfr43ipcE=";

    private static final String MERCHANT_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDUbQNhWV4mhZZseqTrzXSInYCF\n" +
            "lPGi9WcAuxUviGloXVDuAzGkDgURAPnSHB930wDNr6ftVY4UJYoEjKr9DCBs+IIy\n" +
            "4IwVlaLR8IfYFTtjvF+RWs6rqsPO1mwqPmqKSHWtboSw1K3lFIPqO5BAX4jm81uU\n" +
            "hOsi2fG/TADzaVEZvwIDAQAB\n" +
            "-----END PUBLIC KEY-----\n";
    
    private static final String appId = "2020031817080851687";
    private static final String secret = "dVLulrMjORizeuXxdmRWNyizlQwygb";

    private static HqpayClient hqpayClient;

    private static String out_trade_no;
    private static String out_refund_no;
    private static String time_expire;

    private static String getNo() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + (int) (Math.random() * 900 + 100);
    }

    @BeforeClass
    public static void init() {
        hqpayClient = new HqpayClient(appId, secret, MERCHANT_PRIVATE_KEY);
        hqpayClient.setHost("http://jicheng.upaypal.cn");//商户在生产环境需要注释这行
        out_trade_no = getNo();
        out_refund_no = getNo();
        Calendar cal = Calendar.getInstance();  
        cal.add(Calendar .YEAR, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time_expire = simpleDateFormat.format(cal.getTime());
    }

    @Test
    public void test1CreateTrade() throws Exception {

        List<Map<String, Object>> dis_invoices = Arrays.asList(
                new HashMap<String, Object>() {{
                    put("order_type", "trade");//trade 普通交易单 适用于个人用户购买场景
                    put("out_trade_no", out_trade_no);
                    put("amount", 0.02);
                    put("subject", "条码支付订单");
                    put("time_expire", time_expire);
                    
	                Map<String,Object> offerMap = new HashMap<>();
	                offerMap.put("product_id", "\"t12345\"");
	                offerMap.put("product_name", "\"西溪湿地承认门票\"");
	                offerMap.put("quantity", 1);
	                offerMap.put("unit_price", 0.01);
                    put("offer",offerMap);
                    
                    //以下为当面付场景需要额外增加的支付条码数字信息
//	                put("channel","SCAN");
//	                Map<String,Object> credentialMap = new HashMap<>();
//	                credentialMap.put("auth_code", "134924015146425833");//支付码，支付宝或微信付款码数字
//                    put("credential",credentialMap);
                    
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
    public void test4CreateRefundBill() throws Exception {
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
    public void test6Cancel() throws Exception {
        System.out.println("out_trade_no:" + out_trade_no);
        Map<String, Object> cancel = new HashMap<String, Object>() {{
            put("out_trade_no", out_trade_no);
            put("cancel_reason", "系统自动取消测试");
        }};
        System.out.println("取消交易单:" + hqpayClient.sendOne(cancel, HqpayResource.CANCEL));
    }

}
