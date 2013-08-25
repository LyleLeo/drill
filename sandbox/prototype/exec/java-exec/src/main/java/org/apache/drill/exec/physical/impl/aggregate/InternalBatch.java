package org.apache.drill.exec.physical.impl.aggregate;

import java.util.Iterator;

import org.apache.drill.exec.record.BatchSchema;
import org.apache.drill.exec.record.RecordBatch;
import org.apache.drill.exec.record.VectorContainer;
import org.apache.drill.exec.record.VectorWrapper;
import org.apache.drill.exec.record.selection.SelectionVector2;
import org.apache.drill.exec.record.selection.SelectionVector4;

public class InternalBatch implements Iterable<VectorWrapper<?>>{
  static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(InternalBatch.class);
  
  private final VectorContainer container;
  private final BatchSchema schema;
  private final SelectionVector2 sv2;
  private final SelectionVector4 sv4;
  
  public InternalBatch(RecordBatch incoming){
    switch(incoming.getSchema().getSelectionVectorMode()){
    case FOUR_BYTE:
      this.sv4 = incoming.getSelectionVector4().createNewWrapperCurrent();
      this.sv2 = null;
      break;
    case TWO_BYTE:
      this.sv4 = null;
      this.sv2 = incoming.getSelectionVector2().clone(); 
      break;
    default:
      this.sv4 = null;
      this.sv2 = null;
    }
    this.schema = incoming.getSchema();
    this.container = VectorContainer.getTransferClone(incoming);
  }

  public BatchSchema getSchema() {
    return schema;
  }

  public SelectionVector2 getSv2() {
    return sv2;
  }

  public SelectionVector4 getSv4() {
    return sv4;
  }

  @Override
  public Iterator<VectorWrapper<?>> iterator() {
    return container.iterator();
  }

  public void clear(){
    if(sv2 != null) sv2.clear();
    if(sv4 != null) sv4.clear();
    container.clear();
  }
  
  public VectorWrapper<?> getValueAccessorById(int fieldId, Class<?> clazz){
    return container.getVectorAccessor(fieldId, clazz);
  }
  
}
