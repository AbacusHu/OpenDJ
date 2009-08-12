package org.opends.ldap.extensions;



import org.opends.ldap.DecodeException;
import org.opends.ldap.requests.GenericExtendedRequest;
import org.opends.ldap.requests.Requests;
import org.opends.ldap.responses.GenericExtendedResult;
import org.opends.ldap.responses.Responses;
import org.opends.server.types.ByteString;
import org.opends.spi.ExtendedOperation;
import org.opends.types.ResultCode;



/**
 * Created by IntelliJ IDEA. User: digitalperk Date: Jun 19, 2009 Time:
 * 8:38:43 PM To change this template use File | Settings | File
 * Templates.
 */
public final class GenericExtendedOperation implements
    ExtendedOperation<GenericExtendedRequest, GenericExtendedResult>
{
  private static final GenericExtendedOperation SINGLETON =
      new GenericExtendedOperation();



  public static GenericExtendedOperation getInstance()
  {
    return SINGLETON;
  }



  protected GenericExtendedOperation()
  {
  }



  public GenericExtendedRequest decodeRequest(String requestName,
      ByteString requestValue) throws DecodeException
  {
    return Requests.newGenericExtendedRequest(requestName, requestValue);
  }



  public GenericExtendedResult decodeResponse(ResultCode resultCode,
      String matchedDN, String diagnosticMessage, String responseName,
      ByteString responseValue) throws DecodeException
  {
    return Responses.newGenericExtendedResult(resultCode).setMatchedDN(
        matchedDN).setDiagnosticMessage(diagnosticMessage)
        .setResponseName(responseName).setResponseValue(responseValue);
  }



  public GenericExtendedResult decodeResponse(ResultCode resultCode,
      String matchedDN, String diagnosticMessage)
  {
    return Responses.newGenericExtendedResult(resultCode).setMatchedDN(
        matchedDN).setDiagnosticMessage(diagnosticMessage);
  }
}
