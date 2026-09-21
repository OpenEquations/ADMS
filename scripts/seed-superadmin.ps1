<#
.SYNOPSIS
  Seeds the very first ADMS account (which the backend automatically makes
  SUPERADMIN, since POST /api/users requires no auth while the users table
  is empty). Refuses to run if any user already exists.

.EXAMPLE
  ./scripts/seed-superadmin.ps1 -Email you@example.com -Password 'S0meStrongPass!'

.EXAMPLE
  ./scripts/seed-superadmin.ps1 -FirstName Joe -LastName Lebonheur `
    -Email joe@example.com -Password 'S0meStrongPass!' -ApiUrl http://localhost:8081
#>
param(
    [string]$ApiUrl = $(if ($env:ADMS_API_URL) { $env:ADMS_API_URL } else { "http://localhost:8081" }),
    [string]$FirstName = "Admin",
    [string]$LastName = "User",
    [string]$Email = "admin@example.com",
    [string]$Password = "ChangeMe123!"
)

$ErrorActionPreference = "Stop"

if ($Email -eq "admin@example.com" -or $Password -eq "ChangeMe123!") {
    Write-Host "WARNING: using a placeholder email/password. Pass your own:"
    Write-Host "  ./scripts/seed-superadmin.ps1 -Email you@example.com -Password 'YourPassword'"
    Write-Host ""
}

Write-Host "Checking for existing users at $ApiUrl ..."
try {
    $existing = Invoke-RestMethod -Uri "$ApiUrl/api/users" -Method Get
} catch {
    Write-Host "Could not reach $ApiUrl/api/users - is the backend running?"
    exit 1
}

if ($existing.Count -gt 0) {
    Write-Host "Users already exist in the database - bootstrap only works for the"
    Write-Host "very first account. Use the Users page (logged in as a Superadmin)"
    Write-Host "or a Bearer-token curl/Invoke-RestMethod call to add more accounts."
    exit 1
}

Write-Host "No users found. Creating the first account as SUPERADMIN ..."
$body = @{
    firstName = $FirstName
    lastName  = $LastName
    email     = $Email
    password  = $Password
} | ConvertTo-Json

try {
    $created = Invoke-RestMethod -Uri "$ApiUrl/api/users" -Method Post -ContentType "application/json" -Body $body
    Write-Host "Superadmin account created:"
    $created | ConvertTo-Json
    Write-Host ""
    Write-Host "Log in with: email=$Email  password=$Password"
} catch {
    $errorBody = $_.ErrorDetails.Message
    Write-Host "Failed to create account: $errorBody"
    exit 1
}
