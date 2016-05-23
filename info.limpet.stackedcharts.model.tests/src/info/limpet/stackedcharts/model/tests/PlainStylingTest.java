/**
 */
package info.limpet.stackedcharts.model.tests;

import info.limpet.stackedcharts.model.PlainStyling;
import info.limpet.stackedcharts.model.StackedchartsFactory;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Plain Styling</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class PlainStylingTest extends StylingTest
{

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static void main(String[] args)
  {
    TestRunner.run(PlainStylingTest.class);
  }

  /**
   * Constructs a new Plain Styling test case with the given name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PlainStylingTest(String name)
  {
    super(name);
  }

  /**
   * Returns the fixture for this Plain Styling test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected PlainStyling getFixture()
  {
    return (PlainStyling)fixture;
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
    setFixture(StackedchartsFactory.eINSTANCE.createPlainStyling());
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

} //PlainStylingTest
