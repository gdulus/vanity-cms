package vanity.cms.search

public interface ReIndexer {

    public void start()

    public void stop()

    public ReIndexingStatus getStatus()

}