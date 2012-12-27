package clusterer;

import client.SerializableRMOperatorDescription;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;

public class DummyRMOperator extends Operator {
	
	public DummyRMOperator() {
		// needed for de-serialization
		super(SerializableRMOperatorDescription.getOperatorDescription());
	}

	DummyRMOperator(OperatorDescription rapidminerOperatorDescription) {
		super(rapidminerOperatorDescription);
	}
	
}
