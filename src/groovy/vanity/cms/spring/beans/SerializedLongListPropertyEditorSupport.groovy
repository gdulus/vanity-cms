package vanity.cms.spring.beans

import java.beans.PropertyEditorSupport

class SerializedLongListPropertyEditorSupport extends PropertyEditorSupport{

    @Override
    void setAsText(final String s) throws IllegalArgumentException {

    }

    @Override
    String getAsText() {
        return super.getAsText()
    }
}
