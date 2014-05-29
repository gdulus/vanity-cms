package vanity.cms.article

import grails.validation.Validateable

@Validateable
class UpdateTagRelationsCmd {

    Long id

    String parentsToAddSerialized

    String childrenToAddSerialized

    String parentsToDeleteSerialized

    String childrenToDeleteSerialized

    static constraints = {
        id(nullable: false)
    }

    public List<Long> getParentsToAdd() {
        deSerialize(parentsToAddSerialized)
    }

    public List<Long> getChildrenToAdd() {
        deSerialize(childrenToAddSerialized)
    }

    public List<Long> getParentsToDelete() {
        deSerialize(parentsToDeleteSerialized)
    }

    public List<Long> getChildrenToDelete() {
        deSerialize(childrenToDeleteSerialized)
    }

    private List<Long> deSerialize(final String serialized) {
        serialized ? serialized.split(',').collect { it.toLong() } : Collections.emptyList()
    }

}
