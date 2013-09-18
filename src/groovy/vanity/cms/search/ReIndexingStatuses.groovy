package vanity.cms.search

import vanity.search.Index

class ReIndexingStatuses {

    private static final ReIndexingStatus NULL_STATUS = new ReIndexingStatus(0, ReIndexingPhase.NONE)

    private final Map<Index, ReIndexingStatus> statuses = [:]

    void putAt(final Index index, final ReIndexingStatus status){
        statuses[index] = status
    }

    ReIndexingStatus getAt(final Index index){
        statuses[index] ?: NULL_STATUS
    }
}
