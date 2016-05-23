/**
 */
package info.limpet.stackedcharts.model.tests;

import info.limpet.stackedcharts.model.LinearStyling;
import info.limpet.stackedcharts.model.StackedchartsFactory;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Linear Styling</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class LinearStylingTest extends StylingTest
{

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static void main(String[] args)
  {
    TestRunner.run(LinearStylingTest.class);
  }

  /**
   * Constructs a new Linear Styling test case with the given name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LinearStylingTest(String name)
  {
    super(name);
  }

  /**
   * Returns the fixture for this Linear Styling test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected LinearStyling getFixture()
  {
    return (LinearStyling)fixture;
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
    setFixture(StackedchartsFactory.eINSTANCE.createLinearStyling());
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

} //LinearStylingTest
