/**
 */
package info.limpet.stackedcharts.model.tests;

import info.limpet.stackedcharts.model.StackedchartsFactory;
import info.limpet.stackedcharts.model.Zone;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Zone</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class ZoneTest extends AbstractAnnotationTest
{

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static void main(String[] args)
  {
    TestRunner.run(ZoneTest.class);
  }

  /**
   * Constructs a new Zone test case with the given name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ZoneTest(String name)
  {
    super(name);
  }

  /**
   * Returns the fixture for this Zone test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected Zone getFixture()
  {
    return (Zone)fixture;
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
    setFixture(StackedchartsFactory.eINSTANCE.createZone());
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

} //ZoneTest
