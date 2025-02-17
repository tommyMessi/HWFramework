package ohos.com.sun.org.apache.bcel.internal.generic;

import ohos.com.sun.org.apache.bcel.internal.Constants;
import ohos.com.sun.org.apache.bcel.internal.ExceptionConstants;

public class PUTSTATIC extends FieldInstruction implements ExceptionThrower, PopInstruction {
    PUTSTATIC() {
    }

    public PUTSTATIC(int i) {
        super(Constants.PUTSTATIC, i);
    }

    @Override // ohos.com.sun.org.apache.bcel.internal.generic.Instruction, ohos.com.sun.org.apache.bcel.internal.generic.StackConsumer
    public int consumeStack(ConstantPoolGen constantPoolGen) {
        return getFieldSize(constantPoolGen);
    }

    @Override // ohos.com.sun.org.apache.bcel.internal.generic.ExceptionThrower
    public Class[] getExceptions() {
        Class[] clsArr = new Class[(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length + 1)];
        System.arraycopy(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION, 0, clsArr, 0, ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length);
        clsArr[ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length] = ExceptionConstants.INCOMPATIBLE_CLASS_CHANGE_ERROR;
        return clsArr;
    }

    @Override // ohos.com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor visitor) {
        visitor.visitExceptionThrower(this);
        visitor.visitStackConsumer(this);
        visitor.visitPopInstruction(this);
        visitor.visitTypedInstruction(this);
        visitor.visitLoadClass(this);
        visitor.visitCPInstruction(this);
        visitor.visitFieldOrMethod(this);
        visitor.visitFieldInstruction(this);
        visitor.visitPUTSTATIC(this);
    }
}
