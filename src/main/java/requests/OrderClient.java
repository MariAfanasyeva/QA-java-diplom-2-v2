package requests;
import deserializator.order.get.Orders;
import deserializator.order.get.UserOrders;
import io.restassured.response.Response;
import serialization.Order;
import specs.RequestSpec;
import urls.Endpoints;
import java.util.List;


public class OrderClient extends RequestSpec {
    private Endpoints endpoints = new Endpoints();


    public Response createOrder(Order order, String accessToken) throws InterruptedException {
        return baseSpec()
                .auth().oauth2(accessToken)
                .when()
                .baseUri(endpoints.baseUri)
                .body(order)
                .when()
                .post(endpoints.orderUri);
    }

    public UserOrders getOrdersOfUser(String accessToken) throws InterruptedException {
        return baseSpec()
                .auth().oauth2(accessToken)
                .when()
                .baseUri(endpoints.baseUri)
                .when()
                .get(endpoints.orderUri)
                .body()
                .as(UserOrders.class);
    }

    public Response getUserOrder(String accessToken) throws InterruptedException {
        return baseSpec()
                .auth().oauth2(accessToken)
                .when()
                .baseUri(endpoints.baseUri)
                .when()
                .get(endpoints.orderUri);
    }

    public int checkUserOrdersIds(UserOrders userOrders){
        List<Orders> orderDataList = userOrders.getOrders();
        Orders ordersItem;
        int idCounter = 0;
        for (Orders orders : orderDataList){
            ordersItem = orders;
            if (ordersItem.get_id() != null){
                idCounter++;
            }
        }
        return idCounter;
    }
}
