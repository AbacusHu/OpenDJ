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
 */
package org.opends.server.types;

import org.forgerock.i18n.LocalizableMessage;

/**
 * This class defines a base exception that should be extended by any exception
 * that exposes a unique identifier for the associated message.
 */
public abstract class IdentifiedException extends OpenDsException {
    /**
     * Generated serialization ID.
     */
    private static final long serialVersionUID = 7071843225564003122L;

    /**
     * Creates a new identified exception.
     */
    protected IdentifiedException() {
        super();
    }

    /**
     * Creates a new identified exception with the provided information.
     *
     * @param message
     *            The message that explains the problem that occurred.
     */
    protected IdentifiedException(LocalizableMessage message) {
        super(message);
    }

    /**
     * Creates a new identified exception with the provided information.
     *
     * @param cause
     *            The underlying cause that triggered this exception.
     */
    protected IdentifiedException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new identified exception with the provided information.
     *
     * @param message
     *            The message that explains the problem that occurred.
     * @param cause
     *            The underlying cause that triggered this exception.
     */
    protected IdentifiedException(LocalizableMessage message, Throwable cause) {
        super(message, cause);
    }

}