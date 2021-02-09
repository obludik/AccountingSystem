package accounting.data;

/**
 * Transfer object for sending a result JSON data to client.
 */
public class Result {

    ResponseDescription responseDescription;

    public Result(){
    }

    public Result(ResponseDescription responseDescription) {
        this.responseDescription = responseDescription;
    }

    public ResponseDescription getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(ResponseDescription responseDescription) {
        this.responseDescription = responseDescription;
    }
}
