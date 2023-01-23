package app.foot.controller.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonPropertyOrder({
    Exception.JSON_PROPERTY_TYPE,
    Exception.JSON_PROPERTY_MESSAGE
})
@ToString
@EqualsAndHashCode
public class Exception {
  public static final String JSON_PROPERTY_TYPE = "type";
  public static final String JSON_PROPERTY_MESSAGE = "message";
  private String type;
  private String message;

  public Exception() {
  }

  public Exception type(String type) {
    this.type = type;
    return this;
  }

  @JsonProperty(JSON_PROPERTY_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getType() {
    return type;
  }


  @JsonProperty(JSON_PROPERTY_TYPE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setType(String type) {
    this.type = type;
  }


  public Exception message(String message) {
    this.message = message;
    return this;
  }

  @JsonProperty(JSON_PROPERTY_MESSAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getMessage() {
    return message;
  }


  @JsonProperty(JSON_PROPERTY_MESSAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setMessage(String message) {
    this.message = message;
  }


}


