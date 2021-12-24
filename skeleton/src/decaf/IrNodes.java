package decaf;

abstract class Ir {};
class IrClassDecl extends Ir {
    private String name;
    void setName(String n) {
        name=n;
    }
};

abstract class IrExpression extends Ir {};
abstract class IrLiteral extends IrExpression {};
class IrIntLiteral extends IrLiteral {};

// TODO: add remaining classes