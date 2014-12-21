package com.malmstein.hnews.inject;

import com.malmstein.hnews.base.DeveloperError;
import com.malmstein.hnews.comments.CommentsRetriever;
import com.malmstein.hnews.feed.DatabasePersister;
import com.malmstein.hnews.feed.StoriesProvider;
import com.malmstein.hnews.http.ConnectionProvider;

public class Inject {

    private static Inject INSTANCE;
    private final StoriesProvider storiesProvider;
    private final CommentsRetriever commentsRetriever;
    private final ConnectionProvider connectionProvider;

    private Inject(StoriesProvider storiesProvider, CommentsRetriever commentsRetriever, ConnectionProvider connectionProvider) {
        this.storiesProvider = storiesProvider;
        this.commentsRetriever = commentsRetriever;
        this.connectionProvider = connectionProvider;
    }

    public static void using(DependenciesFactory factory) {
        ConnectionProvider connectionProvider = factory.createConnectionProvider();
        DatabasePersister databasePersister = factory.createDatabasePersister();

        StoriesProvider storiesProvider = factory.createStoriesProvider(databasePersister);
        CommentsRetriever commentsRetriever = factory.createCommentsRetriever(databasePersister, connectionProvider);

        INSTANCE = new Inject(storiesProvider, commentsRetriever, connectionProvider);
    }

    private static Inject instance() {
        if (INSTANCE == null) {
            throw new DeveloperError("You need to setup Inject to use a valid DependenciesFactory");
        }
        return INSTANCE;
    }

    public static StoriesProvider storiesProvider() {
        return instance().storiesProvider;
    }

    public static CommentsRetriever commentsRetriever() { return instance().commentsRetriever; }

    public static ConnectionProvider connectionProvider() {
        return instance().connectionProvider;
    }

}
