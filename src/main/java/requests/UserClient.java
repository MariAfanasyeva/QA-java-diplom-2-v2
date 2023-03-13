package requests;

import deserializator.user.UserResponse;
import io.restassured.response.Response;
import serialization.User;
import specs.RequestSpec;
import urls.Endpoints;

//все запросы для работы с апи юзера
public class UserClient extends RequestSpec {
    private Endpoints endpoints = new Endpoints();
    private String token;
    private String accessToken;

    //создаем юзера и возвращаем ответ от сервера
    public Response createUser(User user) throws InterruptedException {
    return baseSpec()
            .baseUri(endpoints.baseUri)
            .body(user)
            .when()
            .post(endpoints.registerUri);
   }

   //авторизуемся за юзера и возвращаем ответ от сервера
   public Response loginUser(User user) throws InterruptedException {
        return baseSpec()
                .baseUri(endpoints.baseUri)
                .body(user)
                .when()
                .post(endpoints.loginUri);
   }
   //установка токена от Response
    public void setToken(Response response) {
        String tmpToken = response.then().extract().path("accessToken");
        this.accessToken = tmpToken.replace("Bearer ", "");
    }

    //установка токена из десериализации
    public void setAccessToken(UserResponse userResponse) {
        this.accessToken = userResponse.getAccessToken().replace("Bearer ", "");
    }

    public String getAccessToken() {
        return accessToken;
    }

    //удаление юзера
    public void deleteUser(String token) throws InterruptedException {
        if (token != null){
            baseSpec()
                    .auth().oauth2(token)
                    .baseUri(endpoints.baseUri)
                    .delete(endpoints.authUser).then().assertThat().statusCode(202);
        }
    }
    //проверка данных юзера
    public UserResponse checkUserCreds(User user) throws InterruptedException {
       return baseSpec()
                .baseUri(endpoints.baseUri)
                .body(user)
                .when()
                .post(endpoints.registerUri)
                .as(UserResponse.class);
    }

    //изменение данных юзера
    public Response changeUserData(User user, String token) throws InterruptedException {
        return baseSpec()
                .auth().oauth2(token)
                .baseUri(endpoints.baseUri)
                .body(user)
                .when()
                .patch(endpoints.authUser);
    }
}
