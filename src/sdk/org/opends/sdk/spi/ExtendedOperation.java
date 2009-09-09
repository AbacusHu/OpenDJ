package org.opends.sdk.spi;



import org.opends.sdk.DecodeException;
import org.opends.sdk.ExtendedRequest;
import org.opends.sdk.Result;
import org.opends.sdk.ResultCode;
import org.opends.server.types.ByteString;



/**
 * Created by IntelliJ IDEA. User: digitalperk Date: Jun 19, 2009 Time:
 * 8:39:52 PM To change this template use File | Settings | File
 * Templates.
 */
public interface ExtendedOperation<R extends ExtendedRequest<S>, S extends Result>
{
  R decodeRequest(String requestName, ByteString requestValue)
      throws DecodeException;



  S decodeResponse(ResultCode resultCode, String matchedDN,
      String diagnosticMessage);



  S decodeResponse(ResultCode resultCode, String matchedDN,
      String diagnosticMessage, String responseName,
      ByteString responseValue) throws DecodeException;

}
