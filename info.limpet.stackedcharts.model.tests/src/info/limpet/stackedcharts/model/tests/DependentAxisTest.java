/**
 */
package info.limpet.stackedcharts.model.tests;

import info.limpet.stackedcharts.model.DependentAxis;
import info.limpet.stackedcharts.model.StackedchartsFactory;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Dependent Axis</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class DependentAxisTest extends AbstractAxisTest
{

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static void main(String[] args)
  {
    TestRunner.run(DependentAxisTest.class);
  }

  /**
   * Constructs a new Dependent Axis test case with the given name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DependentAxisTest(String name)
  {
    super(name);
  }

  /**
   * Returns the fixture for this Dependent Axis test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected DependentAxis getFixture()
  {
    return (DependentAxis)fixture;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see junit.framework.TestCase#setUp()
   * @generated
   */
  @Override
  protected void setUp() throws Exception
  {
    setFixture(StackedchartsFactory.eINSTANCE.createDependentAxis());
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see junit.framework.TestCase#tearDown()
   * @generated
   */
  @Override
  protected void tearDown() throws Exception
  {
    setFixture(null);
  }

} //DependentAxisTest
