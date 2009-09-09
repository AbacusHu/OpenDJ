/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at
 * trunk/opends/resource/legal-notices/OpenDS.LICENSE
 * or https://OpenDS.dev.java.net/OpenDS.LICENSE.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at
 * trunk/opends/resource/legal-notices/OpenDS.LICENSE.  If applicable,
 * add the following below this CDDL HEADER, with the fields enclosed
 * by brackets "[]" replaced with your own identifying information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Copyright 2009 Sun Microsystems, Inc.
 */

package org.opends.sdk.ldap;



import java.util.concurrent.ExecutorService;

import org.opends.sdk.BindRequest;
import org.opends.sdk.BindResult;
import org.opends.sdk.BindResultFuture;
import org.opends.sdk.Connection;
import org.opends.sdk.Responses;
import org.opends.sdk.ResultCode;
import org.opends.sdk.ResultHandler;



/**
 * Bind result future implementation.
 */
class BindResultFutureImpl extends AbstractResultFutureImpl<BindResult>
    implements BindResultFuture
{
  private final BindRequest request;



  BindResultFutureImpl(int messageID, BindRequest request,
      ResultHandler<BindResult> handler, Connection connection,
      ExecutorService handlerExecutor)
  {
    super(messageID, handler, connection, handlerExecutor);
    this.request = request;
  }



  /**
   * {@inheritDoc}
   */
  BindResult newErrorResult(ResultCode resultCode,
      String diagnosticMessage, Throwable cause)
  {
    return Responses.newBindResult(resultCode).setDiagnosticMessage(
        diagnosticMessage).setCause(cause);
  }



  BindRequest getRequest()
  {
    return request;
  }

}
