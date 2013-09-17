package vanity.cms.search

class ReIndexingStatus {

    final int percent

    final ReIndexingPhase reIndexingPhase

    ReIndexingStatus(int percent, ReIndexingPhase reIndexingPhase) {
        this.percent = percent
        this.reIndexingPhase = reIndexingPhase
    }
}
