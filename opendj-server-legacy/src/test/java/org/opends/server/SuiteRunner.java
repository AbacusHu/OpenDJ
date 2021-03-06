/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at legal-notices/CDDLv1_0.txt
 * or http://forgerock.org/license/CDDLv1.0.html.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at legal-notices/CDDLv1_0.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Copyright 2008 Sun Microsystems, Inc.
 *      Portions Copyright 2014 ForgeRock AS
 */
package org.opends.server;

import org.testng.TestNG;
import static org.opends.server.TestCaseUtils.originalSystemErr;

/**
 * This class wraps TestNG so that we can force the process to exit if there
 * is an uncaught exception (e.g. OutOfMemoryError).
 */
public class SuiteRunner {
  public static void main(String[] args) {
    try {
      TestNG.main(args);
    } catch (Throwable e) {
      originalSystemErr.println("TestNG.main threw an expected exception:");
      e.printStackTrace(originalSystemErr);
      System.exit(1);
    }
  }
}
