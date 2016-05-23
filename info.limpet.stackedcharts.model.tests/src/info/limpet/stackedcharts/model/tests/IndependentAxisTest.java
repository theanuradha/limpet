/**
 */
package info.limpet.stackedcharts.model.tests;

import info.limpet.stackedcharts.model.IndependentAxis;
import info.limpet.stackedcharts.model.StackedchartsFactory;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Independent Axis</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class IndependentAxisTest extends AbstractAxisTest
{

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static void main(String[] args)
  {
    TestRunner.run(IndependentAxisTest.class);
  }

  /**
   * Constructs a new Independent Axis test case with the given name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IndependentAxisTest(String name)
  {
    super(name);
  }

  /**
   * Returns the fixture for this Independent Axis test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected IndependentAxis getFixture()
  {
    return (IndependentAxis)fixture;
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
    setFixture(StackedchartsFactory.eINSTANCE.createIndependentAxis());
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

} //IndependentAxisTest
