package vanity.cms.search.reindexer.impl

import vanity.search.Index

public final class ReIndexingCmd {

    final Index target

    final Closure dataProvider

    ReIndexingCmd(Index target, Closure dataProvider) {
        this.target = target
        this.dataProvider = dataProvider
    }
}