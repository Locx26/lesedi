#!/bin/bash

echo "=== Week 3: Banking System with Full MVC Architecture ==="
echo ""

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_step() {
    echo -e "${BLUE}[STEP]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Java is available
check_java() {
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed or not in PATH"
        return 1
    fi
   
    JAVA_VERSION=$(java -version 2>&1 | head -n1 | cut -d'"' -f2)
    print_success "Java version: $JAVA_VERSION"
    return 0
}

# Run tests
run_tests() {
    print_step "Running unit tests..."
    if ./gradlew test; then
        print_success "All tests passed!"
    else
        print_error "Some tests failed"
        return 1
    fi
}

# Build application
build_app() {
    print_step "Building application..."
    if ./gradlew build; then
        print_success "Build completed successfully"
    else
        print_error "Build failed"
        return 1
    fi
}

# Run application
run_app() {
    print_step "Starting Banking Application..."
    echo ""
    echo -e "${GREEN}=== Application Features ===${NC}"
    echo "• MVC Architecture with layered services"
    echo "• User authentication and role-based access"
    echo "• Account management (create, deposit, withdraw)"
    echo "• Transaction processing and history"
    echo "• Customer onboarding (Teller role)"
    echo "• Professional JavaFX UI"
    echo ""
   
    if ./gradlew run; then
        print_success "Application terminated normally"
    else
        print_error "Application failed to start"
        return 1
    fi
}

# Main execution flow
main() {
    echo "Banking System - Week 3 Implementation"
    echo "MVC Architecture with Complete Business Logic"
    echo ""
   
    # Check dependencies
    if ! check_java; then
        exit 1
    fi
   
    # Run tests
    if ! run_tests; then
        print_warning "Continuing despite test failures..."
    fi
   
    # Build application
    if ! build_app; then
        exit 1
    fi
   
    # Run application
    run_app
}

# Run the main function
main