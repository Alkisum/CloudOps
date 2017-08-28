package com.alkisum.android.cloudops.net.owncloud;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

import com.alkisum.android.cloudops.utils.Notifier;
import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.common.OwnCloudClientFactory;
import com.owncloud.android.lib.common.OwnCloudCredentialsFactory;

/**
 * Base class for ownCloud operations.
 *
 * @author Alkisum
 * @version 1.2
 * @since 1.2
 */
class OcOperator {

    /**
     * Context.
     */
    private final Context context;

    /**
     * OwnCloud client.
     */
    private OwnCloudClient client;

    /**
     * Handler for the operation on the ownCloud server.
     */
    private final Handler handler;

    /**
     * Notifier instance to show notification when doing operations.
     */
    private Notifier notifier;

    /**
     * OcOperator constructor.
     *
     * @param context   Context
     * @param intent    Intent for notification
     * @param channelId Channel id for notification
     * @param icon      Icon for notification
     */
    OcOperator(final Context context, final Intent intent,
               final String channelId, final int icon) {
        this.context = context;
        this.handler = new Handler();

        notifier = new Notifier(context, channelId);
        if (intent != null) {
            notifier.setIntent(context, intent);
        }
        notifier.setIcon(icon);
    }

    /**
     * Initialize ownCloud client with given information.
     *
     * @param address  Server address
     * @param username Username
     * @param password Password
     */
    final void init(final String address, final String username,
                    final String password) {
        Uri serverUri = Uri.parse(address);
        client = OwnCloudClientFactory.createOwnCloudClient(
                serverUri, context, true);
        client.setCredentials(OwnCloudCredentialsFactory.newBasicCredentials(
                username, password));
    }

    /**
     * @return Context
     */
    final Context getContext() {
        return context;
    }

    /**
     * @return ownCloud client
     */
    final OwnCloudClient getClient() {
        return client;
    }

    /**
     * @return Handler for the operation on the ownCloud server
     */
    final Handler getHandler() {
        return handler;
    }

    /**
     * @return Notifier instance to show notification when doing operations
     */
    final Notifier getNotifier() {
        return notifier;
    }
}
