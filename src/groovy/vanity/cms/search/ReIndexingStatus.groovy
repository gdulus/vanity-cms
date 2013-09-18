package vanity.cms.search

class ReIndexingStatus {

    final int percent

    final ReIndexingPhase phase

    ReIndexingStatus(int percent, ReIndexingPhase phase) {
        this.percent = percent
        this.phase = phase
    }
}
